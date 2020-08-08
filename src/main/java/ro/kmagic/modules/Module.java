package ro.kmagic.modules;

public abstract class Module {

    public abstract String getModuleName();
    public abstract ModuleType getModuleType();

    public abstract boolean isEnabled();
    public abstract void setEnabled(boolean bool);

}

