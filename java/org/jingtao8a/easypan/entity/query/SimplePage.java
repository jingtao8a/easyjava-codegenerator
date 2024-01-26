package org.jingtao8a.easypan.entity.query;
import org.jingtao8a.easypan.enums.PageSize;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimplePage {
    private int pageNo;
    private int countTotal;
    private int pageSize;
    private int pageTotal;
    private int start;
    private int end;

    public SimplePage(Integer pageNo, int countTotal, int pageSize) {
        if (pageNo == null) {
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.countTotal = countTotal;
        this.pageSize = pageSize;
        action();
    }
    public SimplePage(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void action() {
        if (pageSize <= 0) {
            pageSize = PageSize.SIZE20.getSize();
        }
        if (countTotal > 0) {
            pageTotal = countTotal % pageSize == 0 ? countTotal / pageSize : countTotal / pageSize + 1;
        } else {
            pageTotal = 1;
        }
        if (pageNo <= 1) {
            pageNo = 1;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }
        start = pageSize * (pageNo - 1);
        end = Math.min(pageSize * pageNo, countTotal);
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
        action();
    }
}
