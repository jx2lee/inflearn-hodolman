<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from 'axios';

const count = ref(0);
const title = ref("");
const content = ref("");

const router = useRouter();

const write = function () {
  console.log(title, content)
  alert(title.value + "/" + content.value)
  axios
      .post("/api/posts", {
        title: title.value,
        content: content.value
  })
  .then(() => {
    router.replace({name: "home"});
  });
};

</script>

<template>
  <div>
    <el-input v-model="title" type="text" placeholder="제목을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" placeholder="내용을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">게시글 작성 완료</el-button>
    </div>
  </div>

</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
}

.content {

}

</style>