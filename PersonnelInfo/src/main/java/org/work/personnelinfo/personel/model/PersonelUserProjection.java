package org.work.personnelinfo.personel.model;


import org.work.personnelinfo.admin.model.Role;

import java.util.Set;

public interface PersonelUserProjection {
    String getName();
    String getSurname();
    Set<Role> getRoles();
}