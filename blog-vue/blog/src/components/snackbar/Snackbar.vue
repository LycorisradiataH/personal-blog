<template>
  <v-snackbar top v-model="visible" :color="color" style="padding: 0px">
    <i :class="icon" style="margin-right: 10px;" />
    {{ this.$store.state.snackbar.message }}
    <!-- 关闭按钮 -->
    <!-- <template v-slot:action="{ attrs }">
      <v-btn v-bind="attrs" v-if="showClose" icon @click="close" :color="color">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </template> -->
  </v-snackbar>
</template>

<script>
export default {
  data () {
    return {}
  },

  computed: {
    visible () {
      return this.$store.state.snackbar.visible
    },
    color () {
      return this.$store.state.snackbar.color
    },
    icon () {
      return this.$store.state.snackbar.icon
    }
    // showClose () {
    //   return this.$store.state.snackbar.showClose
    // }
  },

  methods: {
    // close () {
    //   this.$store.commit('snackbar/CLOSE_SNACKBAR')
    // }
  }
}
</script>
