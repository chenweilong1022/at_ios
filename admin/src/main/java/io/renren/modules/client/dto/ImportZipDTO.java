package io.renren.modules.client.dto;

import lombok.Data;
import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/13 15:09
 */
@Data
public class ImportZipDTO {
    /**
     * 拉群id
     */
    Integer id;

    List<Integer> ids;

    String zipName;
}
