package com.choongang.frombirth_backend.task;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.PhotoService;
import com.choongang.frombirth_backend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class S3SyncTask {
    private final S3Service s3Service;
    private final PhotoService photoService;
    private final ChildrenService childrenService;

    @Autowired
    public S3SyncTask(S3Service s3Service, PhotoService photoService, ChildrenService childrenService) {
        this.s3Service = s3Service;
        this.photoService = photoService;
        this.childrenService = childrenService;
    }

    @Scheduled(cron = "0 0 0 * * WED", zone = "Asia/Seoul") // 매주 수요일 자정 설정
    //@Scheduled(fixedRate = 30000) // 테스트용 30초 간격
    public void syncS3WithDB() {
        System.out.println("sync running...");
        deleteOrphanFiles();
    }

    private void deleteOrphanFiles() {
        // S3에서 모든 파일명 가져오기
        List<String> s3Filenames = s3Service.getFileNamesAll();

        // 모든 Photo와 Children 가져오기
        List<String> photoListFilenames = getPhotoListFilenames();
        List<String> childrenListFilenames = getChildrenListFilenames();

        // S3에서 삭제되지 않은 파일들을 삭제
        deleteFilesNotInDB(s3Filenames, "record/", photoListFilenames);
        deleteFilesNotInDB(s3Filenames, "children/", childrenListFilenames);
    }

    private List<String> getPhotoListFilenames() {
        List<PhotoDTO> photoList = photoService.getAllPhotos();
        return photoList.stream()
                .map(photo -> "record/" + photo.getRecordId() + "/" + photo.getUrl())
                .collect(Collectors.toList());
    }

    private List<String> getChildrenListFilenames() {
        List<ChildrenDTO> childrenList = childrenService.getAllChildren();
        return childrenList.stream()
                .filter(child -> child.getProfilePicture() != null)
                .map(child -> "children/" + child.getChildId() + "/" + child.getProfilePicture())
                .collect(Collectors.toList());
    }

    private void deleteFilesNotInDB(List<String> s3Filenames, String prefix, List<String> dbFilenames) {
        List<String> s3FilesWithPrefix = s3Filenames.stream()
                .filter(file -> file.startsWith(prefix))
                .collect(Collectors.toList());

        // 디버깅용 출력
        System.out.println(prefix + " S3 Filenames: " + s3FilesWithPrefix);
        System.out.println(prefix + " DB Filenames: " + dbFilenames);

        s3FilesWithPrefix.forEach(file -> {
            if (!dbFilenames.contains(file)) {
                System.out.println("Deleting orphan file from S3: " + file);
                s3Service.deleteFile(file);
            }
        });
    }

}
