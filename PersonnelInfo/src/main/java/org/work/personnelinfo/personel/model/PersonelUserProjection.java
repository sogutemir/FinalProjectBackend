package org.work.personnelinfo.personel.model;


import org.work.personnelinfo.admin.model.RoleEntityInfo;

import java.util.Collection;

public interface PersonelUserProjection {
    String getName();
    String getSurname();
    String getRoles();
}