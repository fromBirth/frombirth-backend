package com.choongang.frombirth_backend.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.Projections.*;
import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.model.entity.QPhoto;
import com.choongang.frombirth_backend.model.entity.QRecord;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
                        bean(
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

    @Override
    public Slice<RecordDTO> getRecordPage(Integer childId, Integer recordId, PageRequest pageRequest, String query) {
        QRecord record = QRecord.record;
        QPhoto photo = QPhoto.photo;

        LocalDate recordDate = jpaQueryFactory
                .select(record.recordDate)
                .from(record)
                .where(record.recordId.eq(recordId))
                .fetchOne();

        if (recordDate == null) {
            recordDate = jpaQueryFactory
                    .select(record.recordDate)
                    .from(record)
                    .where(record.childId.eq(childId))
                    .orderBy(record.recordDate.desc())
                    .fetchFirst();  // 가장 최근 날짜의 recordDate 반환
        }
        System.out.println(recordDate);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(record.childId.eq(childId));
        builder.and(record.recordDate.before(recordDate));

        System.out.println(query);
        if (query != null) {
            BooleanBuilder orCondition = new BooleanBuilder();
            orCondition.or(record.title.like("%" + query + "%"));
            orCondition.or(record.content.like("%" + query + "%"));
            builder.and(orCondition);
        }

        List<RecordDTO> records = new JPAQueryFactory(entityManager)
                .from(record)
                .leftJoin(photo)
                .on(record.recordId.eq(photo.recordId))
                .where(builder)
                .orderBy(record.recordDate.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize() + 1)
                .transform(
                        groupBy(record.recordId).list(
                                Projections.constructor(
                                        RecordDTO.class,
                                        record.recordId,
                                        record.childId,
                                        record.recordDate,
                                        record.height,
                                        record.weight,
                                        record.title,
                                        record.content,
                                        record.videoResult,
                                        record.createdAt,
                                        record.updatedAt,
                                        list(
                                                Projections.constructor(
                                                        PhotoDTO.class,
                                                        photo.photoId,
                                                        photo.recordId,
                                                        photo.url,
                                                        photo.createdAt
                                                )
                                        )
                                )
                        )
                );

        boolean hasNextPage = records.size() > pageRequest.getPageSize();
        if (hasNextPage) {
            records.remove(records.size() - 1);
        }

        return new SliceImpl<>(records, pageRequest, hasNextPage);
    }

    @Override
    public RecordDTO findByChildIdAndDate(Integer childId, String date) {
        QRecord record = QRecord.record;
        QPhoto photo = QPhoto.photo;

        // date를 LocalDate로 변환
        LocalDate targetDate = LocalDate.parse(date);

        return jpaQueryFactory.select(
                        Projections.constructor(
                                RecordDTO.class,
                                record.recordId,
                                record.childId,
                                record.recordDate,
                                record.height,
                                record.weight,
                                record.title,
                                record.content,
                                record.videoResult,
                                record.createdAt,
                                record.updatedAt,
                                list(
                                        Projections.fields(
                                                PhotoDTO.class,
                                                photo.photoId,
                                                photo.recordId,
                                                photo.url,
                                                photo.createdAt
                                        )
                                )
                        )
                )
                .from(record)
                .leftJoin(photo)
                .on(record.recordId.eq(photo.recordId))
                .where(record.childId.eq(childId).and(record.recordDate.eq(targetDate))) // childId와 date 조건 추가
                .fetchOne(); // 단일 결과 반환
    }

    @Override  
    public Slice<MonthRecordPhotoDTO> getRecordPhotoByMonth(Integer childId, LocalDate lastMonth,
                                                            PageRequest pageRequest, String query) {
        QRecord record = QRecord.record;
        QPhoto photo = QPhoto.photo;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(record.childId.eq(childId));
        builder.and(record.recordDate.before(lastMonth));

        if (query != null) {
            BooleanBuilder orCondition = new BooleanBuilder();
            orCondition.or(record.title.like("%" + query + "%"));
            orCondition.or(record.content.like("%" + query + "%"));
            builder.and(orCondition);
        }

        List<MonthRecordPhotoDTO> list = jpaQueryFactory
                .from(photo)
                .join(record)
                .on(record.recordId.eq(photo.recordId))
                .where(builder)
                .orderBy(record.recordDate.desc())  // recordDate 기준 내림차순 정렬
                .transform(
                        groupBy(stringTemplate("YEAR({0}) || '-' || MONTH({0})", record.recordDate))
                                .list(
                                        Projections.constructor(
                                                MonthRecordPhotoDTO.class,
                                                stringTemplate("YEAR({0}) || '-' || MONTH({0})", record.recordDate).as("month"),
                                                list(
                                                        Projections.constructor(
                                                                PhotoDTO.class,
                                                                photo.photoId,
                                                                photo.recordId,
                                                                photo.url,
                                                                photo.createdAt
                                                        )
                                                )
                                        )
                                )
                );

// 최종적으로 offset과 limit은 transform 이후에 적용
        int offset = (int) pageRequest.getOffset();
        int pageSize = pageRequest.getPageSize();
        if (list.size() > pageSize) {
            list = list.subList(offset, offset + pageSize);
        }


        boolean hasNext = list.size() > pageRequest.getPageSize();
        if (hasNext) {
            list.remove(list.size() - 1);
        }

        return new SliceImpl<>(list, pageRequest, hasNext);
    }
}
