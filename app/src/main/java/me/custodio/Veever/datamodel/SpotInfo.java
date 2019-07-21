package me.custodio.Veever.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Andrews on 17,May,2019
 */

public class SpotInfo extends RealmObject {

    @SerializedName("defaultLanguage")
    public String defaultLanguage;

    @SerializedName("ptBR")
    public Spot portuguese;

    @SerializedName("enUS")
    public Spot english;

    public Spot getDefaultLanguage() {
        if (defaultLanguage.equals("ptBR")) {
            return portuguese;
        } else {
            return english;
        }
    }

    public LanguageType getLangugewType() {
        if (defaultLanguage.equals("ptBR")) {
            return LanguageType.PORTUGUESE;
        } else {
            return LanguageType.ENGLISH;
        }
    }

}
