export type SortDirection = 'asc' | 'desc'
export type SortField =
    | 'rank'
    | 'totalScore'
    | 'modelName'
    | 'releaseDate'
    | 'driving'
    | 'travel'
    | 'knowledge'
    | 'basic'
    | 'creation'
    | 'quickScene'
    | 'entertainment'

export interface SortConfig {
    field: SortField
    direction: SortDirection
}

export interface RankingDataItem {
    rank: number
    brand: string
    model: string
    modelName: string
    totalScore: number
    releaseDate: string
    useType: string
    scores: {
        [key: string]: number
        driving: number
        travel: number
        knowledge: number
        basic: number
        creation: number
        quickScene: number
        entertainment: number
        voice: number
        nlu: number
        multimodal: number
        assist: number
        control: number
        safety: number
        navigation: number
        traffic: number
        parking: number
        database: number
        search: number
        qa: number
        content: number
        design: number
        edit: number
        scene: number
        command: number
        automation: number
        music: number
        video: number
        game: number
    }
    vehicleType: string
}

export const timeOptions = [
    { value: 'all', label: '全部时间' },
    { value: '2024-01', label: '2024年1月' },
    { value: '2023-12', label: '2023年12月' },
    { value: '2023-11', label: '2023年11月' }
]

export const scoreRangeOptions = [
    { value: 'all', label: '全部分数' },
    { value: '90-100', label: '90-100分' },
    { value: '80-90', label: '80-90分' },
    { value: '70-80', label: '70-80分' }
]

export const baseRankingData: RankingDataItem[] = [
    {
        rank: 1,
        brand: '乐道',
        model: 'L60',
        modelName: 'XiaoLe GPT',
        totalScore: 92.5,
        releaseDate: '2025-01-16',
        useType: '模型',
        vehicleType: 'ledao-l60',
        scores: {
            driving: 62.4,
            travel: 72,
            knowledge: 82,
            basic: 79.4,
            creation: 73,
            quickScene: 71.2,
            entertainment: 72,
            ...generateSubScores(93),
            culture: 98.0,
            generalKnowledge: 91.0,
            safety: 84.62,
            nluAccuracy: 73.33,
            informationExtraction: 85.71,
            languageReasoning: 65.45,
            crossLanguageUnderstanding: 89.0,
            rejectionValidity: 57.44,
            wakeupAccuracy: 70.3,
            taskCompletionRate: 32.17,
            crossDomainCollaboration: 77.28,
            firstCharResponseTime: 56.0,
            textGenerationSpeed: 67.2,
            imageGenerationSpeed: 81.11,

            trafficKnowledge: 82.67,
            automotiveKnowledge: 79.0,
            textGenerationQuality: 81.54,
            motoringSkills: 90.0,
            directInstructionRecognitionRate: 63.08,

            travel_ecology: 63.64,
            complex_command_recognition: 73.33,
            ambiguous_intent_recognition: 52.63,
            context_memory: 50.0,
            life_ecology: 64.44,
            text_generation_quality: 77.14,
            direct_command_recognition: 83.34,

            yule_complex_command_recognition: 59.68,
            yule_ambiguous_intent_recognition: 76.78,
            yule_context_memory: 74.6,
            yule_direct_command_recognition: 72.0,

            xianliao_complex_command_recognition: 87.14,
            xianliao_response_quality: 85.16,
            xianliao_ambiguous_intent_recognition: 85.45,
            xianliao_emotion_regulation: 77.22,
            xianliao_context_memory: 68.57,
            xianliao_online_search: 91.58,
            xianliao_direct_command_recognition: 81.74,

            chuangzuo_complex_command_recognition: 78.18,
            chuangzuo_response_quality: 61.18,
            chuangzuo_ambiguous_intent_recognition: 76.0,
            chuangzuo_emotion_regulation: 57.89,
            chuangzuo_image_generation_quality: 70.0,
            chuangzuo_direct_command_recognition: 86.0,

            chekongyu_complex_command_recognition: 70.48,
            chekongyu_ambiguous_intent_recognition: 53.75,
            chekongyu_modality_richness: 37.5,
            chekongyu_direct_command_recognition: 94.0
        }
    },
    {
        rank: 2,
        brand: '极氪',
        model: '007',
        modelName: 'Zeekr AI Assistant',
        totalScore: 90.8,
        releaseDate: '2024-12-12',
        useType: '模型',
        vehicleType: 'zeekr-007',
        scores: {
            driving: 68.2,
            travel: 62.4,
            knowledge: 66.8,
            basic: 66,
            creation: 63.8,
            quickScene: 57.8,
            entertainment: 61.2,
            ...generateSubScores(91),
            culture: 81.82,
            generalKnowledge: 87.0,
            safety: 74.09,
            nluAccuracy: 80.89,
            informationExtraction: 80.56,
            languageReasoning: 84.21,
            crossLanguageUnderstanding: 60.89,
            rejectionValidity: 69.5,
            wakeupAccuracy: 74.0,
            taskCompletionRate: 76.5,
            crossDomainCollaboration: 58.89,
            firstCharResponseTime: 80.32,
            textGenerationSpeed: 77.46,
            imageGenerationSpeed: 72.0,

            trafficKnowledge: 79.38,
            automotiveKnowledge: 70.0,
            textGenerationQuality: 44.67,
            motoringSkills: 64.5,
            directInstructionRecognitionRate: 75.71,

            travel_ecology: 77.86,
            complex_command_recognition: 68.57,
            ambiguous_intent_recognition: 63.53,
            context_memory: 83.33,
            life_ecology: 63.81,
            text_generation_quality: 92.5,
            direct_command_recognition: 64.29,

            yule_complex_command_recognition: 52.63,
            yule_ambiguous_intent_recognition: 75.0,
            yule_context_memory: 69.0,
            yule_direct_command_recognition: 57.78,

            xianliao_complex_command_recognition: 75.56,
            xianliao_response_quality: 88.24,
            xianliao_ambiguous_intent_recognition: 93.85,
            xianliao_emotion_regulation: 69.66,
            xianliao_context_memory: 84.71,
            xianliao_online_search: 66.96,
            xianliao_direct_command_recognition: 56.3,

            chuangzuo_complex_command_recognition: 89.09,
            chuangzuo_response_quality: 72.0,
            chuangzuo_ambiguous_intent_recognition: 68.0,
            chuangzuo_emotion_regulation: 77.14,
            chuangzuo_image_generation_quality: 78.18,
            chuangzuo_direct_command_recognition: 62.5,

            chekongyu_complex_command_recognition: 81.76,
            chekongyu_ambiguous_intent_recognition: 61.58,
            chekongyu_modality_richness: 43.08,
            chekongyu_direct_command_recognition: 85.94
        }
    }
]

export const industryAverageData: RankingDataItem = {
    rank: -1, // Special rank for industry average
    brand: '行业',
    model: '均值',
    modelName: '行业平均水平',
    totalScore: 85.5,
    releaseDate: '-',
    useType: '均值',
    vehicleType: 'industry-average',
    scores: {
        driving: 86,
        travel: 85,
        knowledge: 84,
        basic: 85,
        creation: 83,
        quickScene: 87,
        entertainment: 85,
        ...generateSubScores(85)
    }
}

function generateSubScores(baseScore: number) {
    return {
        voice: baseScore - 1,
        nlu: baseScore - 2,
        multimodal: baseScore - 3,
        assist: baseScore + 1,
        control: baseScore,
        safety: baseScore - 1,
        navigation: baseScore + 1,
        traffic: baseScore,
        parking: baseScore - 1,
        database: baseScore,
        search: baseScore - 1,
        qa: baseScore - 2,
        content: baseScore - 1,
        design: baseScore - 2,
        edit: baseScore - 3,
        scene: baseScore + 1,
        command: baseScore,
        automation: baseScore - 1,
        music: baseScore,
        video: baseScore - 1,
        game: baseScore - 2
    }
}
