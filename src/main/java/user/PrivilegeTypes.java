package user;

/**
 * <h1>This enumeration represents privilege levels a user may have.</h1>
 * <ul>
 *     <li>God mode - every operation is allowed.</li>
 *     <li>Read only - only reading (listing files) is allowed.</li>
 *     <li>Read write - both reading and writing is allowed. (Listing files, creating, deleting, downloading) </li>
 *     <li>None - no operation is allowed.</li>
 * </ul>
 */
public enum PrivilegeTypes {
    GodMode,
    ReadOnly,
    ReadWrite,
    None
}
