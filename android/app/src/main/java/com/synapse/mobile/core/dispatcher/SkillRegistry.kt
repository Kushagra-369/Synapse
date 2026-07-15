package com.synapse.mobile.core.dispatcher

import com.synapse.mobile.core.actions.Skill

class SkillRegistry {

    private val skills = HashMap<String, Skill>()

    fun register(skill: Skill) {
        skills[skill.name] = skill
    }

    fun get(name: String): Skill? {
        return skills[name]
    }

    fun getAll(): List<Skill> {
        return skills.values.toList()
    }
}