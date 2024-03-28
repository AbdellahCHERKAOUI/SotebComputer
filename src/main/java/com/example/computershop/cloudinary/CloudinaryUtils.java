package com.example.computershop.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtils {
    public static Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnyo9ln0i",
                "api_key", "313172655855799",
                "api_secret", "iHTDUpJ_Atwxa3D6dDx9hoAC_4s"));
    }
}
