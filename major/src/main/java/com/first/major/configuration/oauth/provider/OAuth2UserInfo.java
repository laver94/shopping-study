package com.first.major.configuration.oauth.provider;

public interface OAuth2UserInfo {

  String getProviderId();

  String getProvider();

  String getEmail();

  String getFirstName();

  String getLastName();


}
