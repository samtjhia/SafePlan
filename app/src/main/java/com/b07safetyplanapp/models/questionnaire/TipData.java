package com.b07safetyplanapp.models.questionnaire;

import java.util.Map;

public class TipData {
    private String tip_type;
    private String general_tip;
    private Map<String, String> option_tips;

    public TipData(){}

    //Getters and Setters
    public String getTip_type() {
        return tip_type;
    }
    public void setTip_type(String tip_type) {
        this.tip_type = tip_type;
    }
    public String getGeneral_tip() {
        return general_tip;
    }
    public void setGeneral_tip(String general_tip) {
        this.general_tip = general_tip;
    }
    public Map<String, String> getOption_tips() {
        return option_tips;
    }
    public void setOption_tips(Map<String, String> option_tips) {
        this.option_tips = option_tips;
    }
}

