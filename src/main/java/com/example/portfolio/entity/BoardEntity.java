package com.example.portfolio.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "member")
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid",nullable = false)
    private MemberEntity member;  // 외래 키 관계 설정

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "conte", length = 3500, nullable = false)
    private String conte;

    @Column(name = "hit", nullable = false, columnDefinition = "int default 0")
    private int hit = 0;

    @Column(name = "likes", nullable = false, columnDefinition = "int default 0")
    private int likes = 0;

    @Column(name = "liked_user_ids", length = 2000) // 좋아요한 사용자 ID 목록 저장
    private String likedUserIds = ""; // 초기에 빈 문자열로 설정

    @Column(name = "regdate", nullable = false)
    private LocalDateTime regdate;

    // likedUserIds에 사용자 ID가 포함되어 있는지 확인하는 메서드
    public boolean hasUserLiked(String userid) {
        if (likedUserIds == null) {
            likedUserIds = ""; // null일 경우 빈 문자열로 설정
        }
        return likedUserIds.contains("," + userid + ",");
    }

    // likedUserIds에 새로운 사용자 ID를 추가하는 메서드
    public void addUserIdToLikes(String userid) {
        if (!hasUserLiked(userid)) {
            likedUserIds += "," + userid + ",";
            likes++;  // 좋아요 수 증가
        }
    }
    // likedUserIds에서 사용자 ID를 제거하는 메서드
    public void removeUserIdFromLikes(String userid) {
        if (likedUserIds != null && hasUserLiked(userid)) {
            likedUserIds = likedUserIds.replace("," + userid + ",", ","); // 사용자 ID 제거
            if (likedUserIds.equals(",")) {  // 빈 문자열이 되면 초기화
                likedUserIds = "";
            }
        }
    }

}
