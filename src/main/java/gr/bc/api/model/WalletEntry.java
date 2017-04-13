package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "userId",
            "businessCardId"
        })
public class WalletEntry {
    
    @NotNull
    private long businessCardId;
    @NotNull
    private long userId;
        
    public WalletEntry() {
        super();
    }

    public void setBusinessCardId(long businessCardId) {
        this.businessCardId = businessCardId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public long getBusinessCardId() {
        return businessCardId;
    }

    public long getUserId() {
        return userId;
    }
    
    @Override
    public String toString() {
        return "WalletEntry{"
                + "businessCardId=" + businessCardId 
                + ", userId=" + userId
                + '}';
    }
    
}
