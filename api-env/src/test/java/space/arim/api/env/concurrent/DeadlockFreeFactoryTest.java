/* 
 * ArimAPI-env
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.concurrent;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.arim.omnibus.util.concurrent.CentralisedFuture;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

public class DeadlockFreeFactoryTest {

	private ScheduledExecutorService mainThreadExecutor;
	private FactoryOfTheFuture factory;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@BeforeEach
	public void setup() throws InterruptedException, ExecutionException {
		mainThreadExecutor = Executors.newScheduledThreadPool(1);
		factory = mainThreadExecutor.submit(() -> new FactoryImpl(mainThreadExecutor)).get();
	}
	
	@AfterEach
	public void tearDown() throws InterruptedException {
		((FactoryImpl) factory).cancel();
		mainThreadExecutor.shutdown();
		assertTrue(mainThreadExecutor.awaitTermination(1L, TimeUnit.SECONDS));
	}
	
	@Test
	public void testChains123() {
		factory.runAsync(() -> logger.trace("Job 1: async task")).join();
		logger.trace("Finished Chain1: Job 1");

		factory.runSync(() -> logger.trace("Job 2: main thread task")).join();
		logger.trace("Finished Chain2: Job 2");

		factory.runSync(() -> logger.debug("Job 3.1: main thread task"))
			.thenRunAsync(() -> logger.debug("Job 3.2: back to async"))
			.thenRunSync(() -> logger.debug("Job 3.3: back to main thread")).join();
		logger.debug("Finished Chain3: Job 3.1, Job 3.2, Job 3.3");
	}
	
	@Test
	public void testChain4() {
		factory.runSync(() -> {
			logger.debug("Chain 4: chain starting on main thread wanders async then resynchronises");

			CentralisedFuture<?> chain4Future = factory.runAsync(() -> {
				logger.debug("Job 4.1: start");
				try {
					TimeUnit.SECONDS.sleep(1L);
				} catch (InterruptedException ex) {
					fail(ex);
				}
				logger.debug("Job 4.1: end");

			}).thenRunSync(() -> {
				logger.debug("Job 4.2");

			}).orTimeout(5L, TimeUnit.SECONDS);
			logger.debug("Awaiting completion of chain4");
			chain4Future.join();
			logger.info("Finished Chain4 on main thread");

		}).join();
		logger.debug("Finished Chain4 overall");
	}
	
	@Test
	public void testChain5() {
		logger.debug("Starting Chain 5");

		CentralisedFuture<?> mainFuture = factory.runAsync(() -> {
			logger.trace("Chain 5.1: start");
			try {
				TimeUnit.SECONDS.sleep(1L);
				logger.info("Chain 5.1: end success");
			} catch (InterruptedException ex) {
				fail("Chain 5.1: end failure", ex);
			}
		}).thenRunSync(() -> {
			logger.info("Chain 5.2: conducting");
		});

		class OuterCleanup implements Runnable {
			@Override
			public void run() {

				class InnerCleanup implements Runnable {
					@Override
					public void run() {
						logger.debug("InnerCleanup: Starting to wait on 5.1/5.2");
						try {
							mainFuture.orTimeout(6L, TimeUnit.SECONDS).join();
							logger.debug("Succeeded waiting on 5.1/5.2");
						} catch (CompletionException ex) {
							fail("Failed waiting on 5.1/5.2", ex);
						}
					}
				}
				logger.info("Dispatching InnerCleanup");
				CentralisedFuture<?> auxiliaryFuture = factory.runAsync(new InnerCleanup());
				try {
					auxiliaryFuture.orTimeout(8L, TimeUnit.SECONDS).join();
					logger.info("Succeeded waiting on InnerCleanup");
				} catch (CompletionException ex) {
					fail("Failed waiting on InnerCleanup", ex);
				}
			}
		}
		logger.debug("Dispatching OuterCleanup");
		try {
			mainThreadExecutor.submit(new OuterCleanup()).get(10L, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException ex) {
			fail("Failed waiting for OuterCleanup", ex);	
		}
	}
	
}
