const snackbar = {
  namespaced: true,
  state: {
    message: '',
    color: '',
    visible: false,
    // showClose: true,
    timeout: 2000,
    icon: '',
    type: 'normal'
  },
  // 逻辑处理，同步函数
  mutations: {
    OPEN_SNACKBAR (state, options) {
      state.visible = true
      state.message = options.message
      switch (options.type) {
        case 'error':
          state.color = '#E53935'
          state.icon = 'iconfont icon-error'
          break
        case 'success':
          state.color = '#52C41A'
          state.icon = 'iconfont icon-success'
          break
        case 'warning':
          state.color = '#F57C00'
          state.icon = 'iconfont icon-warning'
          break
        default:
          state.color = 'grey'
          state.icon = 'iconfont icon-info'
      }
    },
    CLOSE_SNACKBAR (state) {
      state.visible = false
    },
    setShowClose (state, isShow) {
      state.showClose = isShow
    },
    setTimeout (state, timeout) {
      state.timeout = timeout
    }
  },
  // 逻辑处理，异步函数
  actions: {
    openSnackbar (context, options) {
      const timeout = context.state.timeout
      context.commit('OPEN_SNACKBAR', {
        message: options.message,
        type: options.type
      })
      setTimeout(() => {
        context.commit('CLOSE_SNACKBAR')
      }, timeout)
    }
  }
}

export default snackbar
