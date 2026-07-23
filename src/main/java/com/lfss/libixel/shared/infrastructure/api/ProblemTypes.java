package com.lfss.libixel.shared.infrastructure.api;

import java.net.URI;

public final class ProblemTypes {

    private ProblemTypes() {}

    //TODO: implement endpoints

    public static final URI VALIDATION =
            URI.create("https://api.libixel.dev/problems/validation");

    public static final URI ALREADY_EXISTS =
            URI.create("https://api.libixel.dev/problems/already-exist");
}