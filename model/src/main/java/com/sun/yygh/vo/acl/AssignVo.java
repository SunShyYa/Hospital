package com.sun.yygh.vo.acl;

import lombok.Data;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 12:34
 **/
@Data
public class AssignVo {

    private Long roleId;

    private Long[] permissionId;
}
