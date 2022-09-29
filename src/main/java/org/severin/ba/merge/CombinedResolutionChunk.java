package org.severin.ba.merge;

public class CombinedResolutionChunk implements ResolutionChunk {
    private final ResolutionChunk first;
    private final ResolutionChunk second;

    public CombinedResolutionChunk(ResolutionChunk first, ResolutionChunk second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String getText() {
        return this.first.getText() + this.second.getText();
    }
}
