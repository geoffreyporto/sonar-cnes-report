package fr.cnes.sonar.report.params;

import fr.cnes.sonar.report.exceptions.MalformedParameterException;
import fr.cnes.sonar.report.exceptions.MissingParameterException;
import fr.cnes.sonar.report.exceptions.UnknownParameterException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Prepares command line's arguments
 * @author begarco
 */
public class ParamsFactory {

    private static final Logger LOGGER = Logger.getLogger(ParamsFactory.class.getCanonicalName());
    private static final String PARAMETER_START = "--";

    /**
     * Generates a full parameters object from user cli
     * @param args arguments to process
     * @return handled parameters
     * @throws UnknownParameterException A parameter is not known
     * @throws MalformedParameterException A parameter is not correct
     */
    public Params create(String[] args) throws UnknownParameterException, MalformedParameterException, MissingParameterException {
        Params params = new Params();
        String parameter = null;

        loadDefault(params);

        List<String> preProcessedArgs = checkBlank(args);

        for (String arg : preProcessedArgs) {
            if(parameter==null) {
                if(checkParameter(arg)) {
                    parameter = extractParameterName(arg);
                } else {
                    throw new MalformedParameterException(arg);
                }
            } else {
                if(params.contains(parameter)) {
                    params.put(parameter, arg);
                    parameter = null;
                } else {
                    throw new UnknownParameterException(parameter);
                }
            }
        }

        if(parameter!=null) {
            throw new UnknownParameterException(parameter);
        }

        if(params.isReliable()) {
            LOGGER.info("Paramètres traités avec succès.");
        }

        return params;
    }

    /**
     * Handle blanks in parameters
     * @param args list of arguments
     * @return the new arg array
     */
    private List<String> checkBlank(String[] args) {
        List<String> checked = new ArrayList<>();
        List<String> raw = new ArrayList<>();

        // fill out raw
        for (String s : args) {
            raw.add(s);
        }

        // construction of new params
        Iterator it = raw.iterator();
        while(it.hasNext()) {

            // construction of blank separated params
            StringBuilder param = new StringBuilder((String) it.next());
            if(param.toString().startsWith("\"") && !(param.toString().endsWith("\""))) {
                while (it.hasNext() && !(param.toString().endsWith("\""))) {
                    param.append(" ").append(it.next());
                }
            }

            // add the param
            checked.add(param.toString().replaceAll("\"",""));
        }

        return checked;
    }

    /**
     * Check validity of parameter
     * @param param parameter to check
     * @return true if param is correct
     */
    private boolean checkParameter(String param) {
        return param.startsWith(PARAMETER_START) && param.length() > PARAMETER_START.length();
    }

    /**
     * Extract correct name of a parameter
     * @param param name to check
     * @return the correct name
     */
    private String extractParameterName(String param) {
        return param.substring(PARAMETER_START.length());
    }

    /**
     * Load default configuration
     * @param params parameters to set
     */
    private void loadDefault(Params params) {
        params.put("sonar.url", "");
        params.put("sonar.project.id", "");
        params.put("sonar.project.quality.profile", "");
        params.put("sonar.project.quality.gate", "");
        params.put("project.name", "default");
        params.put("report.author", "default");
        params.put("report.date", new Date().toString());
        params.put("report.path", "reports");
        params.put("report.template", "code-analysis-template.docx");
    }
}
