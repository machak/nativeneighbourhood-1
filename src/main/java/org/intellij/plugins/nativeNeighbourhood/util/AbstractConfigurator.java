/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 23.08.2005
 * Time: 22:13:28
 */
package org.intellij.plugins.nativeNeighbourhood.util;

import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.intellij.plugins.nativeNeighbourhood.Configurator;

/**
 * @author frb
 */
public abstract class AbstractConfigurator implements JDOMExternalizable {

	final static Logger log = Logger.getInstance(Configurator.class.getName());

    /*
     * What happens during first start after plugin installation
     * - writeExternal is executed prior to readExternal because the xml-root-element
     *   is missing from other.xml
     * - configSynchronizedWithXML is false then and the initial "default" configuration is used
     */

    private boolean configSynchronizedWithXML = false;

    /**
     * @see #initializeDefaultConfig() so that missing keys in other.xml gets initialized
     * platform-dependent
     */
    protected AbstractConfigurator() {
        //initializeDefaultConfig();
    }

    /**
     * Component should do initialization and communication with another components in this method.
     */
    public void initComponent() {
        if (!configSynchronizedWithXML) {
            initializeDefaultConfig();
        }
    }

    /**
     * Component should dispose system resources or perform another cleanup in this method.
     */
    public void disposeComponent() { }

    protected abstract void readConfig(Element pElement);

    protected abstract void writeConfig(Element pElement);

    protected abstract void initializeDefaultConfig();

    protected abstract void updateConfigPriorWrite();

    public void readExternal(Element pElement) throws InvalidDataException {
	    log.debug("readExternal");
        readConfig(pElement);
        configSynchronizedWithXML = true;
    }

    public void writeExternal(Element pElement) throws WriteExternalException {
	    log.debug("writeExternal");
        // write is executed before read if the plugin config root tag is missing from other.xml
        // in this case we should write the initial configuration
        if (!configSynchronizedWithXML) { // first start
            initializeDefaultConfig();
        } else {
            updateConfigPriorWrite();
        }
        writeConfig(pElement);
        configSynchronizedWithXML = true;
    }


    protected Element createOptionElement(String pName, String pValue) {
        Element tOption;
        tOption = new Element(pName);
        tOption.setAttribute("value", pValue);
        return tOption;
    }

    protected String readOptionValue(Element element, String pName, String pDefault) {
        String tResult = pDefault;
        Element tOption = element.getChild(pName);
        if (tOption != null) {
            tResult = tOption.getAttributeValue("value");
        }
        return tResult;
    }

}
