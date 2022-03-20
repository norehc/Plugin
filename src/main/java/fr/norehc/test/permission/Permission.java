package fr.norehc.test.permission;

public class Permission {
    private String permission;
    private boolean stillExist;

    public Permission(String permission) {
        this.permission = permission;
        this.stillExist = true;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean exist() {
        return stillExist;
    }

    public void delete() {
        stillExist = false;
    }
}
