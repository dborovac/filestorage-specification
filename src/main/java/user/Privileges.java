package user;

public class Privileges {
    private boolean god;
    private boolean read;
    private boolean write;
    private boolean delete;
    private boolean download;
    private PrivilegeTypes type;

    public Privileges(PrivilegeTypes privilegeType) {
        if (privilegeType == PrivilegeTypes.GodMode) {
            this.god = true;
            this.type = privilegeType;
        } else if (privilegeType == PrivilegeTypes.ReadOnly) {
            this.god = false;
            this.read = true;
            this.write = false;
            this.delete = false;
            this.download = true;
            this.type = privilegeType;
        } else if (privilegeType == PrivilegeTypes.ReadWrite) {
            this.god = false;
            this.read = true;
            this.write = true;
            this.delete = true;
            this.download = true;
            this.type = privilegeType;
        } else if (privilegeType == PrivilegeTypes.None) {
            this.god = false;
            this.read = false;
            this.write = false;
            this.delete = false;
            this.download = false;
            this.type = privilegeType;
        }
    }

    public boolean hasReadPrivilege() {
        return god || read;
    }

    public boolean hasWritePrivilege() {
        return god || write;
    }

    public boolean hasDeletePrivilege() {
        return god || delete;
    }

    public boolean hasDownloadPrivilege() {
        return god || download;
    }

    public boolean isGod() { return god; }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public PrivilegeTypes getType() {
        return type;
    }
}
