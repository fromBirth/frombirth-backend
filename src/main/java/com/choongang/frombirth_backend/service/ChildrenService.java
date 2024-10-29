package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;

import java.util.List;

public interface ChildrenService {
    List<ChildrenDTO> getAllChildren(Integer userId);
    ChildrenDTO getChildById(Integer childId);
    void addChild(ChildrenDTO childrenDTO);
    void updateChild(ChildrenDTO childrenDTO);
    void deleteChild(Integer childId);
}
