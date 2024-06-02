package com.monetamedia.Dto;

import lombok.Data;

@Data
public class FollowDto {
    private String currentUser;
    private String followedUser;
    private String unFollowedUser;
}
