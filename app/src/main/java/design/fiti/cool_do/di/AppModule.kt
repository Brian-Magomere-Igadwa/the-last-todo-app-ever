package design.fiti.cool_do.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import design.fiti.cool_do.data.local.AppDatabase
import design.fiti.cool_do.data.local.Converters
import design.fiti.cool_do.data.util.GsonParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideChatsDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, "app_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }
}