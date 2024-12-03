package com.boostcamp.dreamteam.dreamdiary.community.model

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import kotlinx.coroutines.flow.flowOf
import java.time.Instant

data class CommentUi(
    val id: String,
    val author: UserUi,
    val content: String,
    val isLiked: Boolean,
    val createdAt: DisplayableDateTime,
    val isMine: Boolean,
)

fun Comment.toUIState(currentUId: String?): CommentUi =
    CommentUi(
        id = this.id,
        author = UserUi(
            uid = this.uid,
            username = this.author,
            profileImageUrl = this.profileImageUrl,
        ),
        content = this.content,
        isLiked = true,
        createdAt = Instant.ofEpochSecond(this.createdAt).toDisplayableDateTime(),
        isMine = currentUId == this.uid,
    )

internal val commentUiPreview1 = CommentUi(
    id = "1",
    author = userUiPreview1,
    content = """
        댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글
        내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용
    """.trimIndent(),
    isLiked = true,
    createdAt = Instant.ofEpochSecond(1733155061).toDisplayableDateTime(),
    isMine = true,
)

internal val commentUiPreview2 = CommentUi(
    id = "2",
    author = userUiPreview2,
    content = "두얏나점은 검미달의 검꼬허어 윽남산습니다 으경인퉀은, 막악박느히다 단으타후 이난류에서. 브터롸어 훌셔다 아낙마식, 줍이까가는 곰븡앨똔긴흔은 횜지색도 니븧이도. 하둔칭대고라일깅밨 비아릐오다가 모요게갠 익혹게 잠느가 도냐자아야 우먹하덜군 레언어저다 매허가 켕올은. 러이할 가그아맀이라 븓너데데스딘 요돈난닌은 브벖링아느어야 안샜댑비다, 도비에 가즤겐은 격달호버를 연닐으로. 게고를 부흐뎌댸를 디가, 돘판으는 하논잔바자두가 퇭곤처럼, 살즈디디노길. 지다그어 양발구으 저징즌디긴꺈 세엤소긱이 델시 촐고으촹을 멧지를 긴느마 애히이엤안으라. 내샤민노아서 눅닐여러다 런노알 춘앨 딩혼 더슴을 반군퇀저후시를 시흥섰 매네다. 머바사를 사늰켱이 난혐셔에서 하저 이도 바팅므산 시오 호숸훕니다 냐디타닥 기니행.",
    isLiked = false,
    createdAt = Instant.ofEpochSecond(1733165061).toDisplayableDateTime(),
    isMine = true,
)

private val commentUiPreview3 = CommentUi(
    id = "3",
    author = userUiPreview3,
    content = "댓글 내용",
    isLiked = true,
    createdAt = Instant.ofEpochSecond(1733126000).toDisplayableDateTime(),
    isMine = true,
)

internal val commentsUiPreview = listOf(
    commentUiPreview1,
    commentUiPreview2,
    commentUiPreview3,
)

internal val pagingCommentsUiPreview = flowOf(PagingData.from(commentsUiPreview))
