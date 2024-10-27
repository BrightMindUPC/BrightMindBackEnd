package upc.edu.pe.BrightMind.lesson_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.LearningPath;
import upc.edu.pe.BrightMind.lesson_service.model.entities.Lesson;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.User;
import upc.edu.pe.BrightMind.lesson_service.repository.LearningPathRepository;
import upc.edu.pe.BrightMind.lesson_service.repository.LessonRepository;
import upc.edu.pe.BrightMind.lesson_service.repository.UserRepository;
import upc.edu.pe.BrightMind.lesson_service.service.util.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public LearningPathService(LearningPathRepository learningPathRepository,
                               UserRepository userRepository,
                               LessonRepository lessonRepository) {
        this.learningPathRepository = learningPathRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    public LearningPathDTO createLearningPath(CreateLearningPathDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Lesson> lessons = lessonRepository.findAllById(dto.getLessonIds());

        if (lessons.size() != dto.getLessonIds().size()) {
            throw new ResourceNotFoundException("One or more lessons not found");
        }

        LearningPath learningPath = new LearningPath();
        learningPath.setName(dto.getName());
        learningPath.setUser(user);
        learningPath.setLessons(lessons);

        LearningPath savedLearningPath = learningPathRepository.save(learningPath);
        return mapToDTO(savedLearningPath);
    }

    @Transactional
    public LearningPathDTO getLearningPathById(Long id) {
        LearningPath learningPath = learningPathRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning Path not found"));
        return mapToDTO(learningPath);
    }

    @Transactional
    public List<LearningPathDTO> getLearningPathsByUserId(Long userId) {
        List<LearningPath> learningPaths = learningPathRepository.findByUserId(userId);
        return learningPaths.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<LearningPathDTO> getAllLearningPaths() {
        List<LearningPath> learningPaths = learningPathRepository.findAll();
        return learningPaths.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public LearningPathDTO updateLearningPath(Long id, CreateLearningPathDTO dto) {
        LearningPath learningPath = learningPathRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning Path not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Lesson> lessons = lessonRepository.findAllById(dto.getLessonIds());

        if (lessons.size() != dto.getLessonIds().size()) {
            throw new ResourceNotFoundException("One or more lessons not found");
        }

        learningPath.setName(dto.getName());
        learningPath.setUser(user);
        learningPath.setLessons(lessons);

        LearningPath updatedLearningPath = learningPathRepository.save(learningPath);
        return mapToDTO(updatedLearningPath);
    }

    public void deleteLearningPath(Long id) {
        if (!learningPathRepository.existsById(id)) {
            throw new ResourceNotFoundException("Learning Path not found");
        }
        learningPathRepository.deleteById(id);
    }

    private LearningPathDTO mapToDTO(LearningPath learningPath) {
        LearningPathDTO dto = new LearningPathDTO();
        dto.setId(learningPath.getId());
        dto.setName(learningPath.getName());
        dto.setUserId(learningPath.getUser().getId());

        List<LessonDTO> lessonDTOs = learningPath.getLessons().stream()
                .map(this::mapLessonToDTO)
                .collect(Collectors.toList());
        dto.setLessons(lessonDTOs);

        return dto;
    }

    private LessonDTO mapLessonToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setSubject(lesson.getSubject());
        dto.setGrade(lesson.getGrade());
        dto.setContent(lesson.getContent());
        return dto;
    }
}

