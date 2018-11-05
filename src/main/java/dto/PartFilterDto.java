package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PartFilterDto extends SortDto {
    private String number;
    private String name;
    private String vendor;
    private Integer qty;
    private Date shippedAfter;
    private Date shippedBefore;
    private Date receivedAfter;
    private Date receivedBefore;
}
