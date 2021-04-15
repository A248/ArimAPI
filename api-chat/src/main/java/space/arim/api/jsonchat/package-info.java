/**
 * Contains the low-level API for parsing and writing chat messages
 * in the Json.sk format. <br>
 * <br>
 * Most users will prefer to use the Json.sk format in tandem with
 * the Adventure API using {@link space.arim.api.jsonchat.adventure}. <br>
 * <br>
 * <b>Package contents</b> <br>
 * There are several mutable classes not safe for re-use or transfer across threads.
 * Each of these are documented as such. All other classes are immutable and thread safe. <br>
 * <br>
 * Unless otherwise stated, no method may return null and no parameter accepts null values.
 * {@code NullPointerException} may be thrown if this is violated.
 *
 */
package space.arim.api.jsonchat;