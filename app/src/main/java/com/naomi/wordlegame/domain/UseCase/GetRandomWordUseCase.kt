import com.naomi.myapplication.data.WordRepository

class GetRandomWordUseCase(private val wordRepository: WordRepository) {

    fun execute(): String {
        return wordRepository.getRandomWord()
    }
}