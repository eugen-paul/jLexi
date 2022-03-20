package net.eugenpaul.jlexi.resourcesmanager.textformat.textformatter;

public interface FormatterTypeParameter<T> {

    public Class<T> getClazz();

    public String getFunction();
}
