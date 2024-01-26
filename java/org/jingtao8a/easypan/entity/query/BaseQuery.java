package org.jingtao8a.easypan.entity.query;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseQuery {
    private SimplePage simplePage;
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
}
