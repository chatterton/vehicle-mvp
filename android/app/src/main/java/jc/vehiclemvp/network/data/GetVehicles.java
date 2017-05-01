package jc.vehiclemvp.network.data;

import java.util.List;

public class GetVehicles extends BaseResponse {

    Response response;

    class Response extends BaseResponse.Response {
        Result result;

        public class Result {
            List<Vehicle> vehicles;
        }
    }

    @Override
    protected Response getResponse() {
        return response;
    }

    public List<Vehicle> getVehicles() {
        return response.result.vehicles;
    }

    public static class Vehicle {
        String name;
        String vin;
        String model;
        String longName;
        String year;
        String subscriptionStatus;
        List<Manual> manuals;
        String verificationStatus;
        Boolean primary;
        Long lastRemoteStartSuccess;

        LastKnownLocation lastKnownLocation;


        public String getName() {
            return name;
        }

        public String getLongName() {
            if (!longName.isEmpty()) {
                return longName;
            }
            //fall back to model name
            return model;
        }

        public String getVin() {
            return vin;
        }

        public String getModel() {
            return model;
        }

        public String getYear() {
            return year;
        }

        public String getSubscriptionStatus(){
            return subscriptionStatus;
        }

        public boolean isPrimary() {
            return primary;
        }

        public String getVerificationStatus() {
            return verificationStatus;
        }

        public boolean isVerified() {
            return !verificationStatus.equalsIgnoreCase("pending");
        }

        public Long getLastRemoteStartSuccess() {
            return lastRemoteStartSuccess;
        }

        public List<Manual> getManuals() {
            return manuals;
        }

        public static class ThumbnailImg {
            Picker picker;

            public class Picker {
                MobileImage mobile;

                public class MobileImage {
                    String src;
                }
            }
        }

        public LastKnownLocation getLastKnownLocation() {
            return lastKnownLocation;
        }

        public static class LastKnownLocation {

            String latitude;

            String longitude;

            String updatedDate;

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getUpdatedDate() {
                return updatedDate;
            }

            public void setUpdatedDate(String updatedDate) {
                this.updatedDate = updatedDate;
            }
        }

        public static class Manual {
            public String size;
            public String subTitle;
            public String title;
            public String url;
        }
    }

}