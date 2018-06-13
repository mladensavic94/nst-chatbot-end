package rs.ac.bg.fon.chatbot.db.domain;

import com.google.gson.annotations.SerializedName;

public enum Status {

    @SerializedName("OPEN")
    OPEN,
    @SerializedName("PRIHVACENO")
    ACCEPTED,
    @SerializedName("POPUNJENO")
    FULL,
    @SerializedName("ODBIJENO")
    DENIED,
    @SerializedName("DESCRIPTION_MISSING")
    DESCRIPTION_MISSING,
    @SerializedName("DESCRIPTION_REQUESTED")
    DESCRIPTION_REQUESTED
}
