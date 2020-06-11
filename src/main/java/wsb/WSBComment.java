package wsb;

public class WSBComment {

    private String parentID;
    private String commentID;
    private String commentAuthor;
    private String commentDate;
    private int commentUpvotes;
    private String commentText;
    private boolean commentIsEdited;
    private boolean commentIsSubmitter;
    private boolean commentIsStickied;

    public WSBComment(String parentID, String commentID, String author, String date, int upvotes, String text, boolean isEdited, boolean isSubmitter, boolean isStickied) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.commentAuthor = author;
        this.commentDate = date;
        this.commentUpvotes = upvotes;
        this.commentText = text;
        this.commentIsEdited = isEdited;
        this.commentIsSubmitter = isSubmitter;
        this.commentIsStickied = isStickied;
    }

    public String getParentID() {
        return parentID;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public int getCommentUpvotes() {
        return commentUpvotes;
    }

    public String getCommentText() {
        return commentText;
    }

    public boolean isCommentIsEdited() {
        return commentIsEdited;
    }

    public boolean isCommentIsSubmitter() {
        return commentIsSubmitter;
    }

    public boolean isCommentIsStickied() {
        return commentIsStickied;
    }

    @Override
    public String toString() {
        return "WSBComment{" +
                "parentID='" + parentID + '\'' +
                ", commentID='" + commentID + '\'' +
                ", commentAuthor='" + commentAuthor + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", commentUpvotes=" + commentUpvotes +
                ", commentText='" + commentText + '\'' +
                ", commentIsEdited=" + commentIsEdited +
                ", commentIsSubmitter=" + commentIsSubmitter +
                ", commentIsStickied=" + commentIsStickied +
                '}';
    }
}
