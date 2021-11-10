package user;

public class Privileges {
    private boolean god;
    private boolean read;
    private boolean write;
    private boolean delete;
    private boolean download;
    private boolean none;
    private PrivilegeTypes type;

    public Privileges(PrivilegeTypes privilegeType) {
        if (privilegeType == PrivilegeTypes.GodMode) {
            this.god = true;
            this.none = false;
            this.type = PrivilegeTypes.GodMode;
        } else if (privilegeType == PrivilegeTypes.ReadOnly) {
            this.god = false;
            this.read = true;
            this.write = false;
            this.delete = false;
            this.download = false;
            this.none = false;
            this.type = PrivilegeTypes.ReadOnly;
        } else if (privilegeType == PrivilegeTypes.ReadWrite) {
            this.god = false;
            this.read = true;
            this.write = true;
            this.delete = true;
            this.download = true;
            this.none = false;
            this.type = PrivilegeTypes.ReadWrite;
        } else if (privilegeType == PrivilegeTypes.None) {
            this.none = true;
            this.god = false;
            this.type = PrivilegeTypes.None;
        }
    }

    public boolean hasReadPrivilege() {
        return !none || god || read;
    }

    public boolean hasWritePrivilege() {
        return !none || god || write;
    }

    public boolean hasDeletePrivilege() {
        return !none || god || delete;
    }

    public boolean hasDownloadPrivilege() {
        return !none || god || download;
    }

    public boolean isGod() { return god; }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public PrivilegeTypes getType() {
        return type;
    }
}
