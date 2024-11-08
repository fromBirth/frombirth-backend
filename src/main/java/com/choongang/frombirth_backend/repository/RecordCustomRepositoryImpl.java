package com.choongang.frombirth_backend.repository;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.model.entity.QPhoto;
import com.choongang.frombirth_backend.model.entity.QRecord;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RecordCustomRepositoryImpl implements RecordCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    public RecordCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month) {
        QRecord record = QRecord.record;
        QPhoto photo = QPhoto.photo;

        Integer targetYear = Integer.parseInt(month.split("-")[0]);
        Integer targetMonth = Integer.parseInt(month.split("-")[1]);
        BooleanExpression dateContains = numberTemplate(Integer.class, "{0}", record.recordDate.year()).eq(targetYear)
                .and(numberTemplate(Integer.class, "{0}", record.recordDate.month()).eq(targetMonth));

        return jpaQueryFactory.select(
                        Projections.bean(
                                RecordPhotoDTO.class,
                                record.recordId,
                                record.childId,
                                record.recordDate,
                                photo.url.as("photoUrl")
                        )
                )
                .from(record)
                .leftJoin(photo)
                .on(record.recordId.eq(photo.recordId))
                .where(record.childId.eq(childId).and(dateContains))
                .orderBy(record.recordDate.asc())
                .fetch();
    }
}
