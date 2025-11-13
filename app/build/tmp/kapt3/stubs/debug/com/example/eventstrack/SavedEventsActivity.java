package com.example.eventstrack;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J\b\u0010\u000e\u001a\u00020\u000bH\u0002J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/example/eventstrack/SavedEventsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "rvSaved", "Landroidx/recyclerview/widget/RecyclerView;", "progressBar", "Landroid/widget/ProgressBar;", "adapter", "Lcom/example/eventstrack/EventAdapter;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "loadSavedEvents", "removeSavedEvent", "event", "Lcom/example/eventstrack/api/Event;", "app_debug"})
public final class SavedEventsActivity extends androidx.appcompat.app.AppCompatActivity {
    private androidx.recyclerview.widget.RecyclerView rvSaved;
    private android.widget.ProgressBar progressBar;
    private com.example.eventstrack.EventAdapter adapter;
    
    public SavedEventsActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadSavedEvents() {
    }
    
    private final void removeSavedEvent(com.example.eventstrack.api.Event event) {
    }
}