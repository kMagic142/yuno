package ro.kmagic.types;

public interface Module {

    String getModuleName();
    ModuleType getModuleType();

    boolean isEnabled();
    void setEnabled(boolean bool);


}

