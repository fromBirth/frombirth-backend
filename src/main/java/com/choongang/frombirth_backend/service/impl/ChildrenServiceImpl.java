package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.entity.Children;
import com.choongang.frombirth_backend.repository.ChildrenRepository;
import com.choongang.frombirth_backend.service.ChildrenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildrenServiceImpl implements ChildrenService {
    private final ChildrenRepository childrenRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenServiceImpl(ChildrenRepository childrenRepository, ModelMapper modelMapper) {
        this.childrenRepository = childrenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ChildrenDTO> getAllChildren(Integer userId) {
        return childrenRepository.findByUserId(userId).stream()
                .map(child -> modelMapper.map(child, ChildrenDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChildrenDTO getChildById(Integer childId) { // 아이 정보 가져오기
        Children child = childrenRepository.findById(childId).orElseThrow();
        return modelMapper.map(child, ChildrenDTO.class);
    }

    @Override
    public void addChild(ChildrenDTO childrenDTO) { // 프로필생성 
        Children child = modelMapper.map(childrenDTO, Children.class);
        childrenRepository.save(child);
    }

    @Override
    public void updateChild(ChildrenDTO childrenDTO) { // 아이 프로필 업데이트
        // 기존 아이 정보 가져오기
        Children existingChild = childrenRepository.findById(childrenDTO.getChildId())
                .orElseThrow(() -> new EntityNotFoundException("아이를 찾을 수 없습니다."));

        // DTO의 필드를 기존 객체에 조건부 복사 (각각 변경 가능)
        if (childrenDTO.getName() != null) {
            existingChild.setName(childrenDTO.getName());
        }
        if (childrenDTO.getChildOrder() != null) {
            existingChild.setChildOrder(childrenDTO.getChildOrder());
        }
        if (childrenDTO.getBirthDate() != null) {
            existingChild.setBirthDate(childrenDTO.getBirthDate());
        }
        if (childrenDTO.getGender() != null) {
            existingChild.setGender(childrenDTO.getGender());
        }
        if (childrenDTO.getBloodType() != null) {
            existingChild.setBloodType(childrenDTO.getBloodType());
        }
        if (childrenDTO.getBirthWeight() != null) {
            existingChild.setBirthWeight(childrenDTO.getBirthWeight());
        }
        if (childrenDTO.getBirthTime() != null) {
            existingChild.setBirthTime(childrenDTO.getBirthTime());
        }

        // 업데이트 시간 설정
        existingChild.setUpdatedAt(LocalDateTime.now());

        // 변경된 엔티티 저장
        childrenRepository.save(existingChild);
    }



    @Override
    public void deleteChild(Integer childId) {
        childrenRepository.deleteById(childId);
    }
}
