package fr.cnes.sonar.report.input;

import fr.cnes.sonar.report.providers.AbstractDataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Public class which contains all public String used by internal classes
 */
public class StringManager {

    /**
     * Logger for StringManager
     */
    private static final Logger LOGGER = Logger.getLogger(StringManager.class.getCanonicalName());

    /**
     * Message logged when parameters are correctly preprocessed
     */
    public static final String SUCCESSFULLY_PROCESSED_PARAMETERS = "Successfully processed parameters.";
    /**
     * Just an empty string
     */
    public static final String EMPTY = "";
    /**
     * Name of the property giving the quality gate used during the analysis
     */
    public static final String SONAR_PROJECT_QUALITY_GATE = "sonar.project.quality.gate";
    /**
     * Name of the property giving the project's name
     */
    public static final String PROJECT_NAME = "project.name";
    /**
     * Name of the property giving the author's name
     */
    public static final String REPORT_AUTHOR = "report.author";
    /**
     * Name of the property giving the date to print on the report
     */
    public static final String REPORT_DATE = "report.date";
    /**
     * Name of the property saying if we want (yes) to export configuration files or not (no)
     */
    public static final String REPORT_CONF = "report.conf";
    /**
     * Name of the property giving the path to the output
     */
    public static final String REPORT_PATH = "report.path";
    /**
     * Name of the property giving the locale
     */
    public static final String REPORT_LOCALE = "report.locale";
    /**
     * Name of the property giving the path to the docx template
     */
    public static final String REPORT_TEMPLATE = "report.template";
    /**
     * Name of the property giving the path to the xlsx template
     */
    public static final String ISSUES_TEMPLATE = "issues.template";
    /**
     * Date pattern
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * Prefix of all parameters
     */
    public static final String PARAMETER_START = "--";
    /**
     * Name for properties' file about report
     */
    public static final String REPORT_PROPERTIES = "report.properties";
    /**
     * Name of the property giving the server url
     */
    public static final String SONAR_URL = "sonar.url";
    /**
     * Name of the property giving the id of the project to analyze
     */
    public static final String SONAR_PROJECT_ID = "sonar.project.id";
    /**
     * Name of the property giving the path to the xlsx template
     */
    public static final String SONAR_PROJECT_QUALITY_PROFILE = "sonar.project.quality.profile";
    /**
     * Just the word rule
     */
    public static final String RULES = "rules";

    /**
     * Contain all the properties related to the report
     */
    private static Properties properties;

    /**
     * Contains internationalized fields
     */
    private static ResourceBundle messages;

    /**
     * Define the language of the program
     */
    private static Locale currentLocale;

    //
    // Static initialization block for reading .properties
    //
    static {
        // store properties
        properties = new Properties();
        // read the file
        InputStream input = null;

        try {
            // load properties file as a stream
            input = AbstractDataProvider.class.getClassLoader().getResourceAsStream(StringManager.REPORT_PROPERTIES);
            if(input!=null) {
                // load properties from the stream in an adapted structure
                properties.load(input);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if(input!=null) {
                try {
                    // close the stream if necessary (not null)
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        // load internationalized strings, french by default
        changeLocale("fr","FR");
    }

    /**
     * Give the value of the property corresponding to the key passed as parameter.
     * It gives only properties related to the report.
     * @param property Key of the property you want.
     * @return The value of the property you want as a String.
     */
    public static String getProperty(String property) {
        return properties.getProperty(property);
    }

    /**
     * Change the locale and reload messages
     * @param language String in lowercase
     * @param country String in upper case
     */
    public static void changeLocale(String language, String country) {
        // change locale
        currentLocale = new Locale(language,country);
        // reload messages
        messages = ResourceBundle.getBundle("messages", currentLocale);
    }

    /**
     * Return string corresponding to the given key according the locale
     * @param key name of the property in the bundle messages
     * @return a String
     */
    public static String string(String key) {
        return messages.getString(key);
    }
}
