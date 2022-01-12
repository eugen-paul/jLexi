package net.eugenpaul.jlexi.data.formatting;

/**
 * Interface for Document formater
 */
public interface Compositor {

    /**
     * set Objects that will be formatet
     * 
     * @param composition
     */
    public void setComposition(Composition composition);

    /**
     * Do format
     */
    public void compose();
}
