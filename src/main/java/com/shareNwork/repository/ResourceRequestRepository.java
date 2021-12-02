package com.shareNwork.repository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import com.shareNwork.domain.EmployeeSkillProficiency;
import com.shareNwork.domain.ResourceRequest;
import com.shareNwork.domain.ResourceRequestSkillsProficiency;
import com.shareNwork.domain.constants.ResourceRequestStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ResourceRequestRepository implements PanacheRepository<ResourceRequest> {

    @Inject
    EntityManager em;

    @Transactional
    public ResourceRequest updateOrCreate(ResourceRequest shareResourceRequest) throws ParseException {
        if (shareResourceRequest.id == null) {
            shareResourceRequest.setCreatedAt(LocalDateTime.now());
            shareResourceRequest.setStatus(ResourceRequestStatus.PENDING);
            persist(shareResourceRequest);
            addSkillsToResourceRequests(shareResourceRequest.id, shareResourceRequest.getSkillProficiencies());
            return shareResourceRequest;
        } else {
            addSkillsToResourceRequests(shareResourceRequest.id, shareResourceRequest.getSkillProficiencies());
            return em.merge(shareResourceRequest);
        }
    }

    @Transactional
    public List<ResourceRequestSkillsProficiency> getSkillsByRequestId(long id) {
        ResourceRequest resourceRequest = findById(id);
        List<ResourceRequestSkillsProficiency> response = new ArrayList<>();
        if (resourceRequest != null) {
            List<ResourceRequestSkillsProficiency> resourceRequestSkillsProficiencies = ResourceRequestSkillsProficiency.listAll();
            for (ResourceRequestSkillsProficiency resourceRequestSkillsProficiency : resourceRequestSkillsProficiencies) {
                if (resourceRequestSkillsProficiency.getResourceRequest().id.equals(id)) {
                    response.add(resourceRequestSkillsProficiency);
                }
            }
        }
        return response;
    }

    @Transactional
    public ResourceRequest addSkillsToResourceRequests(Long id, List<ResourceRequestSkillsProficiency> employeeSkillProficiencies) throws ParseException {
        ResourceRequest employee = findById(id);
        if (employee == null) {
            throw new NotFoundException();
        } else {
            for (ResourceRequestSkillsProficiency employeeSkillProficiency : employeeSkillProficiencies) {
                if (employeeSkillProficiency.id != null) {
                    updateSkillsOfEmployee(employeeSkillProficiency.id, employeeSkillProficiency);
                } else {
                    employeeSkillProficiency.setResourceRequest(employee);
                    employeeSkillProficiency.persist();
                }
            }
        }
        return employee;
    }

    @Transactional
    public ResourceRequestSkillsProficiency updateSkillsOfEmployee(Long id, ResourceRequestSkillsProficiency employeeSkillProficiency) throws ParseException {
        ResourceRequestSkillsProficiency employeeSkillProficiency1 = ResourceRequestSkillsProficiency.findById(employeeSkillProficiency.id);
        if (employeeSkillProficiency1 == null) {
            throw new NotFoundException();
        }
        if (employeeSkillProficiency.getSkill() != null) {
            employeeSkillProficiency1.setSkill(employeeSkillProficiency.getSkill());
        }
        if (employeeSkillProficiency.getProficiencyLevel() != null) {
            employeeSkillProficiency1.setProficiencyLevel(employeeSkillProficiency.getProficiencyLevel());
        }
        employeeSkillProficiency1.persist();
        return employeeSkillProficiency1;
    }
}