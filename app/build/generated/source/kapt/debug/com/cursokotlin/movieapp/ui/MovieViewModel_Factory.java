// Generated by Dagger (https://dagger.dev).
package com.cursokotlin.movieapp.ui;

import com.cursokotlin.movieapp.ddl.data.MovieRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class MovieViewModel_Factory implements Factory<MovieViewModel> {
  private final Provider<MovieRepository> repositoryProvider;

  public MovieViewModel_Factory(Provider<MovieRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MovieViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MovieViewModel_Factory create(Provider<MovieRepository> repositoryProvider) {
    return new MovieViewModel_Factory(repositoryProvider);
  }

  public static MovieViewModel newInstance(MovieRepository repository) {
    return new MovieViewModel(repository);
  }
}