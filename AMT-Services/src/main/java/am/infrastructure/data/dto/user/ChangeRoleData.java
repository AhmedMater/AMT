//package am.infrastructure.data.dto.user;
//
//import am.main.common.validation.groups.BlankValidation;
//import am.main.common.validation.groups.InvalidValidation;
//import am.main.common.validation.groups.LengthValidation;
//import am.main.common.validation.groups.RequiredValidation;
//import am.shared.common.ValidationErrorMsg.OWNER_USER_ID;
//import am.shared.common.ValidationErrorMsg.USER_ROLE;
//
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//
///**
// * Created by ahmed.motair on 11/25/2017.
// */
//public class ChangeRoleData implements Serializable{
//    @NotNull(message = OWNER_USER_ID.REQUIRED, groups = RequiredValidation.class)
//    @Min(value = 1, message = OWNER_USER_ID.INVALID, groups = InvalidValidation.class)
//    private Integer ownerUserID;
//
////    @NotNull(message = VIEWER_USER_ID.REQUIRED, groups = RequiredValidation.class)
////    @Min(value = 1, message = VIEWER_USER_ID.INVALID, groups = InvalidValidation.class)
////    private Integer viewerUserID;
//
//    @NotNull(message = USER_ROLE.REQUIRED, groups = RequiredValidation.class)
//    @NotEmpty(message = USER_ROLE.EMPTY_STR, groups = BlankValidation.class)
//    @Size(min = 2, max = 2, message = USER_ROLE.LENGTH, groups = LengthValidation.class)
//    private String newRole;
//
//    public ChangeRoleData() {
//    }
//
//    public ChangeRoleData(Integer ownerUserID, Integer viewerUserID, String newRole) {
//        this.ownerUserID = ownerUserID;
////        this.viewerUserID = viewerUserID;
//        this.newRole = newRole;
//    }
//
//    public Integer getOwnerUserID() {
//        return ownerUserID;
//    }
//    public void setOwnerUserID(Integer ownerUserID) {
//        this.ownerUserID = ownerUserID;
//    }
//
////    public Integer getViewerUserID() {
////        return viewerUserID;
////    }
////    public void setViewerUserID(Integer viewerUserID) {
////        this.viewerUserID = viewerUserID;
////    }
//
//    public String getNewRole() {
//        return newRole;
//    }
//    public void setNewRole(String newRole) {
//        this.newRole = newRole;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ChangeRoleData)) return false;
//
//        ChangeRoleData that = (ChangeRoleData) o;
//
//        if (getOwnerUserID() != null ? !getOwnerUserID().equals(that.getOwnerUserID()) : that.getOwnerUserID() != null) return false;
////        if (getViewerUserID() != null ? !getViewerUserID().equals(that.getViewerUserID()) : that.getViewerUserID() != null) return false;
//        return getNewRole() != null ? getNewRole().equals(that.getNewRole()) : that.getNewRole() == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = getOwnerUserID() != null ? getOwnerUserID().hashCode() : 0;
////        result = 31 * result + (getViewerUserID() != null ? getViewerUserID().hashCode() : 0);
//        result = 31 * result + (getNewRole() != null ? getNewRole().hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "ChangeRoleData{" +
//                "ownerUserID = " + ownerUserID +
////                ", viewerUserID = " + viewerUserID +
//                ", newRole = " + newRole +
//                "}\n";
//    }
//}
