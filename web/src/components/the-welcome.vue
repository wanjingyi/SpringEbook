<template>
  <div>
    欢迎光临
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
    name: 'the-welcome',
});
</script>
<style scoped>
  .tip {
    padding: 10px 5px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    background: linear-gradient(white,white) padding-box,repeating-linear-gradient(-45deg, black 0, black 25%, white 0, white 50%) 0/.6em .6em;
    animation:ants 12s linear infinite;
  }
  .tip b{
    color: red;
  }
</style>
