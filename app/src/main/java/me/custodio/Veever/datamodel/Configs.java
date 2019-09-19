package me.custodio.Veever.datamodel;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrews on 19,September,2019
 */

public class Configs {

    public boolean askHelp;

    public Map<String, List<String>> safeWords;

    public Configs() {
    }

    public boolean isAskHelp() {
        return askHelp;
    }

    public Map<String, List<String>> getSafeWords() {
        return safeWords;
    }
}
