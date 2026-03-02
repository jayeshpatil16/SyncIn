export interface UserProfileResponse {
  username: string;
  name: string;
  bio: string;
  email: string;
  followers: number;
  following: number;
  followingMe: boolean;
  followedByMe: boolean;
  avatar_url: string;
}
