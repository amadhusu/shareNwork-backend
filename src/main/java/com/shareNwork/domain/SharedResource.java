package com.shareNwork.domain;

import com.shareNwork.domain.constants.ResourceAvailabilityStatus;
import com.shareNwork.domain.constants.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SharedResource extends Employee {

    @Getter
    @Setter
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    List<EmployeeSkillProficiency> skillProficiencies;

    @Getter
    @Setter
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    List<ResourceRequest> projects;

    @Getter
    @Setter
    private String totalExperience;

    @Getter
    @Setter
    private ResourceAvailabilityStatus status;

    public SharedResource(String firstName, String lastName, String employeeId, String emailId, String designation, String totalExperience, ResourceAvailabilityStatus status) {
        super(firstName, lastName, employeeId, emailId, designation);
        this.totalExperience = totalExperience;
        this.status = status;
    }
}
