package com.pluralsight.data;


import com.pluralsight.models.Profile;

public interface ProfileDao
{
    Profile create(Profile profile);
    Profile getUserById(int id);
    void update(Profile profile);
}
