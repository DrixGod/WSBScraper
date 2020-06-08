package wsb;


public class WSBPost {
    private String title;
    private String flair;
    private String date;
    private int upvotes;
    private double upvoteRatio;
    private String id;
    private boolean isEdited;
    private boolean isLocked;
    private boolean isNSFW;
    private boolean isSpoiler;
    private boolean isStickied;
    private String URl;
    private int commentCount;
    private String Text;

    public WSBPost(String title, String flair, String date, int upvotes, double upvoteRatio, String id, String isEdited, boolean isLocked, boolean isNSFW, boolean isSpoiler, boolean isStickied, String URl, int commentCount, String text) {
        this.title = title;
        this.flair = flair;
        this.date = date;
        this.upvotes = upvotes;
        this.upvoteRatio = upvoteRatio;
        this.id = id;
        this.isEdited = isEdited.equals("True");
        this.isLocked = isLocked;
        this.isNSFW = isNSFW;
        this.isSpoiler = isSpoiler;
        this.isStickied = isStickied;
        this.URl = URl;
        this.commentCount = commentCount;
        Text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getFlair() {
        return flair;
    }

    public String getDate() {
        return date;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public double getUpvoteRatio() {
        return upvoteRatio;
    }

    public String getId() {
        return id;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isNSFW() {
        return isNSFW;
    }

    public boolean isSpoiler() {
        return isSpoiler;
    }

    public boolean isStickied() {
        return isStickied;
    }

    public String getURl() {
        return URl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getText() {
        return Text;
    }

    @Override
    public String toString() {
        return "WSB: {" +
                "title='" + title + '\'' +
                ", flair='" + flair + '\'' +
                ", date='" + date + '\'' +
                ", upvotes=" + upvotes +
                ", upvoteRatio=" + upvoteRatio +
                ", id='" + id + '\'' +
                ", isEdited=" + isEdited +
                ", isLocked=" + isLocked +
                ", isNSFW=" + isNSFW +
                ", isSpoiler=" + isSpoiler +
                ", isStickied=" + isStickied +
                ", URl='" + URl + '\'' +
                ", commentCount=" + commentCount +
                ", Text='" + Text + '\'' +
                " }";
    }
}
