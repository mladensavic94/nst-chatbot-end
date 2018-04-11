package rs.ac.bg.fon.chatbot.db.domain;

import com.google.gson.annotations.SerializedName;

public enum Status {

    @SerializedName("OPEN")
    OPEN,
    @SerializedName("ACCEPTED")
    ACCEPTED,
    @SerializedName("FULL")
    FULL,
    @SerializedName("DENIED")
    DENIED
}
