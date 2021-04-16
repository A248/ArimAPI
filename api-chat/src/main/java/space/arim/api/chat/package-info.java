/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */

/**
 * A chat API designed to represent sections of chat messages
 * which may have coloring, styling, and extra features like
 * tooltips and click events. <br>
 * <br>
 * @deprecated The Adventure API has grown to the point where it
 * would be futile to pursue alternative APIs, merely based on
 * the popularity of the former. Although this API provides
 * a somewhat better abstraction over messages using the Json.sk
 * format or formatting codes, Adventure is far more comprehensive,
 * and a better Json.sk parser ought to be built on top of it. As such,
 * {@link space.arim.api.jsonchat} has been created as a replacement.
 */
package space.arim.api.chat;